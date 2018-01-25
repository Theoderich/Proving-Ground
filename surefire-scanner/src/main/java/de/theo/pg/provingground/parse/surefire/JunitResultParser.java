package de.theo.pg.provingground.parse.surefire;

import de.theo.pg.provingground.input.TestResultInput;
import de.theo.pg.provingground.input.TestRunInput;
import de.theo.pg.provingground.parse.surefire.xsd.Testsuite;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JunitResultParser {

    private static final JAXBContext CONTEXT = initJaxbContext();
    private static final Pattern SUREFIRE_FILENAME_PATTERN = Pattern.compile("^TEST-.+\\.xml$");
    private static final Pattern TEST_NAME_PATTERN = Pattern.compile("(\\w+[\\w.]+)\\.(\\w+)");


    public List<TestRunInput> parse(Path sourcePath) throws IOException {

        Stream<Path> allXmlFiles = Files.find(sourcePath, 1, ((path, basicFileAttributes) -> basicFileAttributes.isRegularFile() && SUREFIRE_FILENAME_PATTERN.matcher(path.getFileName().toString()).matches()));

        return allXmlFiles.flatMap(this::parseFile).collect(Collectors.toList());

    }


    private Stream<TestRunInput> parseFile(Path path) {
        List<TestRunInput> executionsInFile = new ArrayList<>();
        Unmarshaller unmarshaller = getUnmarshaller();

        try (InputStream inputStream = Files.newInputStream(path);) {
            Testsuite unmarshal = (Testsuite) unmarshaller.unmarshal(inputStream);


            for (Testsuite.Testcase testcase : unmarshal.getTestcase()) {
                String classname = testcase.getClassname();
                Matcher matcher = TEST_NAME_PATTERN.matcher(classname);
                if (!matcher.matches()) {
                    throw new IllegalStateException("Invalid classname in surefire report: " + path + " classname was " + classname);
                }
                String pkg = matcher.group(1);
                String clazz = matcher.group(2);
                String testName = testcase.getName();

                TestRunInput testRunInput = new TestRunInput();
                testRunInput.setName(pkg + ":" + clazz + ":" + testName);
                String time = testcase.getTime();
                time = time.replaceAll(",", "");

                BigDecimal timeInSeconds = new BigDecimal(time);
                BigDecimal timeInMillis = timeInSeconds.multiply(BigDecimal.valueOf(1000));
                Duration executionTime = Duration.ofMillis(timeInMillis.longValue());
                testRunInput.setDuration(executionTime);
                if (elementNotNull(testcase.getSystemOut())) {
                    testRunInput.setOutput(testcase.getSystemOut().getValue().toString());
                }

                if (elementNotNull(testcase.getError())) {
                    Testsuite.Testcase.Error error = testcase.getError().getValue();
                    testRunInput.setResult(TestResultInput.FAILED);
                    testRunInput.setErrorMessage(error.getMessage());
                    testRunInput.setErrorType(error.getType());
                    testRunInput.setStacktrace(error.getValue());
                } else if (testcase.getFailure().size() > 0) {
                    testRunInput.setResult(TestResultInput.FAILED);
                    Testsuite.Testcase.Failure failure = testcase.getFailure().get(0);
                    testRunInput.setErrorMessage(failure.getMessage());
                    testRunInput.setErrorType(failure.getType());
                    testRunInput.setStacktrace(failure.getValue());
                } else if (elementNotNull(testcase.getSkipped())) {
                    testRunInput.setResult(TestResultInput.SKIPPED);
                } else {
                    testRunInput.setResult(TestResultInput.SUCCESS);
                }
                executionsInFile.add(testRunInput);
            }

        } catch (IOException | JAXBException e) {
            throw new IllegalStateException("Unable to parse surefire xml file: " + path, e);
        }
        return executionsInFile.stream();
    }

    private boolean elementNotNull(JAXBElement element) {
        return element != null && !element.isNil();
    }

    private Unmarshaller getUnmarshaller() {
        try {
            return CONTEXT.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException("Could not initialize Unmarshaller for surefire xml", e);
        }
    }


    private static JAXBContext initJaxbContext() {
        try {
            return JAXBContext.newInstance("de.theo.pg.provingground.parse.surefire.xsd");
        } catch (JAXBException e) {
            throw new IllegalStateException("Could not initialize JAXBContext for surefire xml", e);
        }
    }


}
