package net.lunglet.concurrent;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public final class HadoopTest {
    public static void main(final String[] args) throws IOException {
//        LocalFileSystem fs = new LocalFileSystem();
//        fs.setConf(new Configuration());
//        fs.create(new Path("hello"));


        Configuration conf = new Configuration();
        conf.set("hadoop.tmp.dir", ".");
//        conf.set("fs.default.name", "file:///");
        conf.set("fs.default.name", "hdfs://localhost:54310");
        FileSystem fs = FileSystem.get(conf);

//        fs.setWorkingDirectory(new Path("C:/home/albert"));
//        System.out.println(Arrays.toString(fs.listPaths(new Path("/"))));

        System.out.println(Arrays.toString(fs.listPaths(new Path("."))));
        System.out.println(Arrays.toString(fs.listPaths(new Path("/"))));

        fs.setWorkingDirectory(new Path("/"));
        System.out.println(Arrays.toString(fs.listPaths(new Path("."))));

        System.out.println(fs.exists(new Path("/wrapper.exe")));

        System.out.println(fs.getLength(new Path("/wrapper.exe")));

        FSDataInputStream in = fs.open(new Path("/wrapper.exe"));
        byte[] buf = new byte[(int) fs.getLength(new Path("/wrapper.exe"))];
        in.readFully(buf);
        in.close();
        fs.close();

        // TODO configure hadoop
        // TODO read a local file
    }
}
