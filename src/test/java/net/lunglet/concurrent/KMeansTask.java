package net.lunglet.concurrent;

public final class KMeansTask extends CacheTask<Object> {
    private static final String CACHE_NAME = String.format("%s.CACHE", KMeansTask.class.getName());

    private static final long serialVersionUID = 1L;

    private final String id;

    public KMeansTask(final String id) {
        super(id, CACHE_NAME);
        this.id = id;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("need to get item called " + id + " from cache");
        Object data = getData();
        System.out.println("working out some stuff");

        String result = "WORKED OUT STUFF WITH: " + data.toString();
        System.out.println("RETURNING " + result);
        return result;
    }

    private Object getData() {
        return getCache().get((Object) id).getObjectValue();
    }
}
