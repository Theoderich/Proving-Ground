package de.theo.pg.provingground;

public enum TestResult {
    SUCCESS(false),
    FAILED(true),
    ERROR(true),
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
}
