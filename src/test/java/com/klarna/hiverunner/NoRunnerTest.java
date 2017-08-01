package com.klarna.hiverunner;

import com.klarna.hiverunner.builder.HiveShellBuilder;
import com.klarna.hiverunner.config.HiveRunnerConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * An example of scaffolding a {@link HiveShell} by hand
 */
public class NoRunnerTest {
    HiveShellContainer shell;

    @Before
    public void setUp() throws IOException {
        TemporaryFolder baseDir = new TemporaryFolder();
        baseDir.create();
        HiveRunnerConfig config = new HiveRunnerConfig();
        HiveServerContext context = new StandaloneHiveServerContext(baseDir, config);
        final HiveServerContainer hiveTestHarness = new HiveServerContainer(context);
        HiveShellBuilder hiveShellBuilder = new HiveShellBuilder();
        hiveShellBuilder.setCommandShellEmulation(config.getCommandShellEmulation());
        hiveShellBuilder.setHiveServerContainer(hiveTestHarness);
        shell = hiveShellBuilder.buildShell();
        shell.start();
    }

    @After
    public void tearDown() {
        shell.tearDown();
        shell.getBaseDir().delete();
    }

    @Test
    public void test() {
        shell.execute("CREATE DATABASE example;\n" +
                "CREATE TABLE example.example(name STRING);\n" +
                "INSERT INTO example.example VALUES('Yu Hua');\n");
        List<Object[]> result = shell.executeStatement("select name from example.example");
        assertEquals(1, result.size());
    }
}
