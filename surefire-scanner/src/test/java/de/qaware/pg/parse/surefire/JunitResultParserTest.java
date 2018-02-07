package de.qaware.pg.parse.surefire;

import de.qaware.pg.input.TestRunInput;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JunitResultParserTest {

    @Test
    public void parse() throws URISyntaxException, IOException {
        JunitResultParser parser = new JunitResultParser();
        URL testSources = ClassLoader.getSystemResource("testdata");
        List<TestRunInput> parse = parser.parse(Paths.get(testSources.toURI()));

        assertThat(parse.size(), is(1));

        TestRunInput testRun = parse.get(0);

        assertThat(testRun.getOutput(), is(equalTo("The System Output")));
        assertThat(testRun.getErrorOutput(), is(equalTo("The Error Output")));

    }
}