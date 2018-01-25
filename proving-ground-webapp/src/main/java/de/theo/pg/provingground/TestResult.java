package de.theo.pg.provingground;

import de.theo.pg.provingground.input.TestResultInput;

public enum TestResult {
    SUCCESS(false),
    FAILED(true),
    SKIPPED(false);

    private final boolean failure;

    TestResult(boolean failure) {
        this.failure = failure;
    }

    public boolean isFailure() {
        return failure;
    }

    public boolean isSuccess(){
        return !failure;
    }

    public static TestResult fromInput(TestResultInput input){
        switch (input){
            case SUCCESS:
                return SUCCESS;
            case FAILED:
                return FAILED;
            case SKIPPED:
                return SKIPPED;
            default:
                throw new IllegalArgumentException("Unknown TestResultInput: " + input);
        }
    }
}
