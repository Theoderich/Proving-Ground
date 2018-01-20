package de.theo.pg.provingground.parse.surefire;

import de.theo.pg.provingground.Test;
import de.theo.pg.provingground.TestExecution;
import de.theo.pg.provingground.TestResult;
import de.theo.pg.provingground.info.ErrorExecutionInfo;
import de.theo.pg.provingground.info.ExecutionInfo;
import de.theo.pg.provingground.info.SuccessExecutionInfo;
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


    public List<TestExecution> parse(Path sourcePath) throws IOException {

        Stream<Path> allXmlFiles = Files.find(sourcePath, 1, ((path, basicFileAttributes) -> basicFileAttributes.isRegularFile() && SUREFIRE_FILENAME_PATTERN.matcher(path.getFileName().toString()).matches()));

        return allXmlFiles.flatMap(this::parseFile).collect(Collectors.toList());

    }


    private Stream<TestExecution> parseFile(Path path) {
        List<TestExecution> executionsInFile = new ArrayList<>();
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

                Test test = new Test(pkg, clazz, testName);
                String time = testcase.getTime();
                time = time.replaceAll(",", "");

                BigDecimal timeInSeconds = new BigDecimal(time);
                BigDecimal timeInMillis = timeInSeconds.multiply(BigDecimal.valueOf(1000));
                Duration executionTime = Duration.ofMillis(timeInMillis.longValue());
                TestResult result;
                ExecutionInfo info;

                String systemOut = "";
                if (elementNotNull(testcase.getSystemOut())) {
                    systemOut = testcase.getSystemOut().getValue().toString();
                }

                if (elementNotNull(testcase.getError())) {
                    result = TestResult.ERROR;
                    Testsuite.Testcase.Error error = testcase.getError().getValue();

                    info = new ErrorExecutionInfo(error.getMessage(), error.getType(), error.getValue(), systemOut);
                } else if (testcase.getFailure().size() > 0) {
                    result = TestResult.FAILED;
                    Testsuite.Testcase.Failure failure = testcase.getFailure().get(0);
                    info = new ErrorExecutionInfo(failure.getMessage(), failure.getType(), failure.getValue(), systemOut);
                } else if (elementNotNull(testcase.getSkipped())) {
                    result = TestResult.SKIPPED;
                    info = new SuccessExecutionInfo(systemOut);
                } else {
                    result = TestResult.SUCCESS;
                    info = new SuccessExecutionInfo(systemOut);
                }
                executionsInFile.add(new TestExecution(test, result, executionTime, info));
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
