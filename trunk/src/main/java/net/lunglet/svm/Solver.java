package net.lunglet.svm;

class Solver {
    // java: information about solution except alpha,
    // because we cannot return multiple values otherwise...
    static class SolutionInfo {
        double obj;

        double r; // for Solver_NU

        double rho;

        double upper_bound_n;

        double upper_bound_p;
    }

    static final byte FREE = 2;

    static final double INF = Double.POSITIVE_INFINITY;

    static final byte LOWER_BOUND = 0;

    static final byte UPPER_BOUND = 1;

    int[] active_set;

    int active_size;

    double[] alpha;

    byte[] alpha_status; // LOWER_BOUND, UPPER_BOUND, FREE

    double Cp, Cn;

    double eps;

    double[] G; // gradient of objective function

    double[] G_bar; // gradient, if we treat free variables as 0

    int l;

    double[] p;

    QMatrix Q;

    float[] QD;

    boolean unshrinked; // XXX

    byte[] y;

    private boolean be_shrunken(int i, double Gmax1, double Gmax2) {
        if (is_upper_bound(i)) {
            if (y[i] == +1) {
                return (-G[i] > Gmax1);
            } else {
                return (-G[i] > Gmax2);
            }
        } else if (is_lower_bound(i)) {
            if (y[i] == +1) {
                return (G[i] > Gmax2);
            } else {
                return (G[i] > Gmax1);
            }
        } else {
            return (false);
        }
    }

    double calculateRho() {
        double r;
        int nr_free = 0;
        double ub = INF, lb = -INF, sum_free = 0;
        for (int i = 0; i < active_size; i++) {
            double yG = y[i] * G[i];

            if (is_lower_bound(i)) {
                if (y[i] > 0) {
                    ub = Math.min(ub, yG);
                } else {
                    lb = Math.max(lb, yG);
                }
            } else if (is_upper_bound(i)) {
                if (y[i] < 0) {
                    ub = Math.min(ub, yG);
                } else {
                    lb = Math.max(lb, yG);
                }
            } else {
                ++nr_free;
                sum_free += yG;
            }
        }

        if (nr_free > 0) {
            r = sum_free / nr_free;
        } else {
            r = (ub + lb) / 2;
        }

        return r;
    }

    void doShrinking() {
        int i;
        double Gmax1 = -INF; // max { -y_i * grad(f)_i | i in I_up(\alpha) }
        double Gmax2 = -INF; // max { y_i * grad(f)_i | i in I_low(\alpha) }

        // find maximal violating pair first
        for (i = 0; i < active_size; i++) {
            if (y[i] == +1) {
                if (!is_upper_bound(i)) {
                    if (-G[i] >= Gmax1) {
                        Gmax1 = -G[i];
                    }
                }
                if (!is_lower_bound(i)) {
                    if (G[i] >= Gmax2) {
                        Gmax2 = G[i];
                    }
                }
            } else {
                if (!is_upper_bound(i)) {
                    if (-G[i] >= Gmax2) {
                        Gmax2 = -G[i];
                    }
                }
                if (!is_lower_bound(i)) {
                    if (G[i] >= Gmax1) {
                        Gmax1 = G[i];
                    }
                }
            }
        }

        // shrink

        for (i = 0; i < active_size; i++) {
            if (be_shrunken(i, Gmax1, Gmax2)) {
                active_size--;
                while (active_size > i) {
                    if (!be_shrunken(active_size, Gmax1, Gmax2)) {
                        swap_index(i, active_size);
                        break;
                    }
                    active_size--;
                }
            }
        }

        // unshrink, check all variables again before final iterations

        if (unshrinked || Gmax1 + Gmax2 > eps * 10) {
            return;
        }

        unshrinked = true;
        reconstructGradient();

        for (i = l - 1; i >= active_size; i--) {
            if (!be_shrunken(i, Gmax1, Gmax2)) {
                while (active_size < i) {
                    if (be_shrunken(active_size, Gmax1, Gmax2)) {
                        swap_index(i, active_size);
                        break;
                    }
                    active_size++;
                }
                active_size++;
            }
        }
    }

    double get_C(final int i) {
        return (y[i] > 0) ? Cp : Cn;
    }

    boolean is_free(final int i) {
        return alpha_status[i] == FREE;
    }

    boolean is_lower_bound(final int i) {
        return alpha_status[i] == LOWER_BOUND;
    }

    boolean is_upper_bound(final int i) {
        return alpha_status[i] == UPPER_BOUND;
    }

    void reconstructGradient() {
        // reconstruct inactive elements of G from G_bar and free variables

        if (active_size == l) {
            return;
        }

        int i;
        for (i = active_size; i < l; i++) {
            G[i] = G_bar[i] + p[i];
        }

        for (i = 0; i < active_size; i++) {
            if (is_free(i)) {
                float[] Q_i = Q.getQ(i, l);
                double alpha_i = alpha[i];
                for (int j = active_size; j < l; j++) {
                    G[j] += alpha_i * Q_i[j];
                }
            }
        }
    }

    // return 1 if already optimal, return 0 otherwise
    int selectWorkingSet(final int[] working_set) {
        // return i,j such that
        // i: maximizes -y_i * grad(f)_i, i in I_up(\alpha)
        // j: mimimizes the decrease of obj value
        // (if quadratic coefficeint <= 0, replace it with tau)
        // -y_j*grad(f)_j < -y_i*grad(f)_i, j in I_low(\alpha)

        double Gmax = -INF;
        double Gmax2 = -INF;
        int Gmax_idx = -1;
        int Gmin_idx = -1;
        double obj_diff_min = INF;

        for (int t = 0; t < active_size; t++) {
            if (y[t] == +1) {
                if (!is_upper_bound(t)) {
                    if (-G[t] >= Gmax) {
                        Gmax = -G[t];
                        Gmax_idx = t;
                    }
                }
            } else {
                if (!is_lower_bound(t)) {
                    if (G[t] >= Gmax) {
                        Gmax = G[t];
                        Gmax_idx = t;
                    }
                }
            }
        }

        int i = Gmax_idx;
        float[] Q_i = null;
        if (i != -1) {
            // null Q_i not accessed: Gmax=-INF if i=-1
            Q_i = Q.getQ(i, active_size);
        }

        for (int j = 0; j < active_size; j++) {
            if (y[j] == +1) {
                if (!is_lower_bound(j)) {
                    double grad_diff = Gmax + G[j];
                    if (G[j] >= Gmax2) {
                        Gmax2 = G[j];
                    }
                    if (grad_diff > 0) {
                        double obj_diff;
                        double quad_coef = Q_i[i] + QD[j] - 2 * y[i] * Q_i[j];
                        if (quad_coef > 0) {
                            obj_diff = -(grad_diff * grad_diff) / quad_coef;
                        } else {
                            obj_diff = -(grad_diff * grad_diff) / 1e-12;
                        }

                        if (obj_diff <= obj_diff_min) {
                            Gmin_idx = j;
                            obj_diff_min = obj_diff;
                        }
                    }
                }
            } else {
                if (!is_upper_bound(j)) {
                    double grad_diff = Gmax - G[j];
                    if (-G[j] >= Gmax2) {
                        Gmax2 = -G[j];
                    }
                    if (grad_diff > 0) {
                        double obj_diff;
                        double quad_coef = Q_i[i] + QD[j] + 2 * y[i] * Q_i[j];
                        if (quad_coef > 0) {
                            obj_diff = -(grad_diff * grad_diff) / quad_coef;
                        } else {
                            obj_diff = -(grad_diff * grad_diff) / 1e-12;
                        }

                        if (obj_diff <= obj_diff_min) {
                            Gmin_idx = j;
                            obj_diff_min = obj_diff;
                        }
                    }
                }
            }
        }

        if (Gmax + Gmax2 < eps) {
            return 1;
        }

        working_set[0] = Gmax_idx;
        working_set[1] = Gmin_idx;
        return 0;
    }

    void Solve(final int l, final QMatrix Q, final double[] p_, final byte[] y_, final double[] alpha_,
            final double Cp, final double Cn, final double eps, final SolutionInfo si, final int shrinking) {
        this.l = l;
        this.Q = Q;
        QD = Q.getQD();
        p = p_.clone();
        y = y_.clone();
        alpha = alpha_.clone();
        this.Cp = Cp;
        this.Cn = Cn;
        this.eps = eps;
        this.unshrinked = false;

        // initialize alpha_status
        {
            alpha_status = new byte[l];
            for (int i = 0; i < l; i++) {
                update_alpha_status(i);
            }
        }

        // initialize active set (for shrinking)
        {
            active_set = new int[l];
            for (int i = 0; i < l; i++) {
                active_set[i] = i;
            }
            active_size = l;
        }

        // initialize gradient
        {
            G = new double[l];
            G_bar = new double[l];
            int i;
            for (i = 0; i < l; i++) {
                G[i] = p[i];
                G_bar[i] = 0;
            }
            for (i = 0; i < l; i++) {
                if (!is_lower_bound(i)) {
                    float[] Q_i = Q.getQ(i, l);
                    double alpha_i = alpha[i];
                    int j;
                    for (j = 0; j < l; j++) {
                        G[j] += alpha_i * Q_i[j];
                    }
                    if (is_upper_bound(i)) {
                        for (j = 0; j < l; j++) {
                            G_bar[j] += get_C(i) * Q_i[j];
                        }
                    }
                }
            }
        }

        // optimization step

        int iter = 0;
        int counter = Math.min(l, 1000) + 1;
        int[] workingSet = new int[2];

        while (true) {
            // show progress and do shrinking

            if (--counter == 0) {
                counter = Math.min(l, 1000);
                if (shrinking != 0) {
                    doShrinking();
                }
            }

            if (selectWorkingSet(workingSet) != 0) {
                // reconstruct the whole gradient
                reconstructGradient();
                // reset active set size and check
                active_size = l;
                if (selectWorkingSet(workingSet) != 0) {
                    break;
                } else {
                    // do shrinking next iteration
                    counter = 1;
                }
            }

            int i = workingSet[0];
            int j = workingSet[1];

            ++iter;

            // update alpha[i] and alpha[j], handle bounds carefully

            float[] Q_i = Q.getQ(i, active_size);
            float[] Q_j = Q.getQ(j, active_size);

            double C_i = get_C(i);
            double C_j = get_C(j);

            double old_alpha_i = alpha[i];
            double old_alpha_j = alpha[j];

            if (y[i] != y[j]) {
                double quad_coef = Q_i[i] + Q_j[j] + 2 * Q_i[j];
                if (quad_coef <= 0) {
                    quad_coef = 1e-12;
                }
                double delta = (-G[i] - G[j]) / quad_coef;
                double diff = alpha[i] - alpha[j];
                alpha[i] += delta;
                alpha[j] += delta;

                if (diff > 0) {
                    if (alpha[j] < 0) {
                        alpha[j] = 0;
                        alpha[i] = diff;
                    }
                } else {
                    if (alpha[i] < 0) {
                        alpha[i] = 0;
                        alpha[j] = -diff;
                    }
                }
                if (diff > C_i - C_j) {
                    if (alpha[i] > C_i) {
                        alpha[i] = C_i;
                        alpha[j] = C_i - diff;
                    }
                } else {
                    if (alpha[j] > C_j) {
                        alpha[j] = C_j;
                        alpha[i] = C_j + diff;
                    }
                }
            } else {
                double quad_coef = Q_i[i] + Q_j[j] - 2 * Q_i[j];
                if (quad_coef <= 0) {
                    quad_coef = 1e-12;
                }
                double delta = (G[i] - G[j]) / quad_coef;
                double sum = alpha[i] + alpha[j];
                alpha[i] -= delta;
                alpha[j] += delta;

                if (sum > C_i) {
                    if (alpha[i] > C_i) {
                        alpha[i] = C_i;
                        alpha[j] = sum - C_i;
                    }
                } else {
                    if (alpha[j] < 0) {
                        alpha[j] = 0;
                        alpha[i] = sum;
                    }
                }
                if (sum > C_j) {
                    if (alpha[j] > C_j) {
                        alpha[j] = C_j;
                        alpha[i] = sum - C_j;
                    }
                } else {
                    if (alpha[i] < 0) {
                        alpha[i] = 0;
                        alpha[j] = sum;
                    }
                }
            }

            // update G

            double delta_alpha_i = alpha[i] - old_alpha_i;
            double delta_alpha_j = alpha[j] - old_alpha_j;

            for (int k = 0; k < active_size; k++) {
                G[k] += Q_i[k] * delta_alpha_i + Q_j[k] * delta_alpha_j;
            }

            // update alpha_status and G_bar

            {
                boolean ui = is_upper_bound(i);
                boolean uj = is_upper_bound(j);
                update_alpha_status(i);
                update_alpha_status(j);
                int k;
                if (ui != is_upper_bound(i)) {
                    Q_i = Q.getQ(i, l);
                    if (ui) {
                        for (k = 0; k < l; k++) {
                            G_bar[k] -= C_i * Q_i[k];
                        }
                    } else {
                        for (k = 0; k < l; k++) {
                            G_bar[k] += C_i * Q_i[k];
                        }
                    }
                }

                if (uj != is_upper_bound(j)) {
                    Q_j = Q.getQ(j, l);
                    if (uj) {
                        for (k = 0; k < l; k++) {
                            G_bar[k] -= C_j * Q_j[k];
                        }
                    } else {
                        for (k = 0; k < l; k++) {
                            G_bar[k] += C_j * Q_j[k];
                        }
                    }
                }
            }

        }

        // calculate rho

        si.rho = calculateRho();

        // calculate objective value
        double v = 0;
        for (int i = 0; i < l; i++) {
            v += alpha[i] * (G[i] + p[i]);
        }

        si.obj = v / 2;

        // put back the solution
        for (int i = 0; i < l; i++) {
            alpha_[active_set[i]] = alpha[i];
        }

        si.upper_bound_p = Cp;
        si.upper_bound_n = Cn;

//        log.info("optimization finished, #iter = " + iter);
    }

    void swap_index(final int i, final int j) {
        Q.swapIndex(i, j);
        do {
            byte other = y[i];
            y[i] = y[j];
            y[j] = other;
        } while (false);
        do {
            double other = G[i];
            G[i] = G[j];
            G[j] = other;
        } while (false);
        do {
            byte other = alpha_status[i];
            alpha_status[i] = alpha_status[j];
            alpha_status[j] = other;
        } while (false);
        do {
            double other = alpha[i];
            alpha[i] = alpha[j];
            alpha[j] = other;
        } while (false);
        do {
            double other = p[i];
            p[i] = p[j];
            p[j] = other;
        } while (false);
        do {
            int other = active_set[i];
            active_set[i] = active_set[j];
            active_set[j] = other;
        } while (false);
        do {
            double other = G_bar[i];
            G_bar[i] = G_bar[j];
            G_bar[j] = other;
        } while (false);
    }

    void update_alpha_status(final int i) {
        if (alpha[i] >= get_C(i)) {
            alpha_status[i] = UPPER_BOUND;
        } else if (alpha[i] <= 0) {
            alpha_status[i] = LOWER_BOUND;
        } else {
            alpha_status[i] = FREE;
        }
    }
}
