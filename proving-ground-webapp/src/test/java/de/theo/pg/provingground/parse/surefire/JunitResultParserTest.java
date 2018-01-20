package de.theo.pg.provingground.parse.surefire;

import de.theo.pg.provingground.TestExecution;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class JunitResultParserTest {

    @Test
    public void parse() throws URISyntaxException, IOException {
        Path testPath = Paths.get(ClassLoader.getSystemResource("input").toURI());
        JunitResultParser junitResultParser = new JunitResultParser();
        List<TestExecution> parse = junitResultParser.parse(testPath);
        System.out.println(parse);

    }
}