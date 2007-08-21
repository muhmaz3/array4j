package net.lunglet.hdf;

abstract class IdComponent {
    protected int id;

    public IdComponent(final int id) {
        this.id = id;
    }

    final int getId() {
        return id;
    }
}
