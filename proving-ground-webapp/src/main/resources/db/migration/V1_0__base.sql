CREATE SEQUENCE s_id
  START WITH 1;

CREATE TABLE project (
  id     BIGINT       NOT NULL,
  name   VARCHAR(255) NOT NULL,
  status INT          NOT NULL,

  CONSTRAINT pk_project PRIMARY KEY (ID),
  CONSTRAINT u_project_name UNIQUE (name)
);

CREATE TABLE testsuite (
  id            BIGINT       NOT NULL,
  fk_project_id BIGINT       NOT NULL,
  name          VARCHAR(255) NOT NULL,
  start_time    TIMESTAMP    NOT NULL,
  status        INT          NOT NULL,
  num_total     INT          NOT NULL,
  num_success   INT          NOT NULL,
  num_failed    INT          NOT NULL,
  num_skipped   INT          NOT NULL,
  CONSTRAINT pk_testsuite PRIMARY KEY (ID),
  CONSTRAINT fk_testsuite_project_id FOREIGN KEY (fk_project_id) REFERENCES project (id),
);

CREATE TABLE testrun (
  id              BIGINT       NOT NULL,
  fk_testsuite_id BIGINT       NOT NULL,
  test_name       VARCHAR(510) NOT NULL,
  result          INT          NOT NULL,
  duration        BIGINT       NOT NULL,
  output          CLOB,
  errorType       VARCHAR(510),
  errorMessage    CLOB,
  stacktrace      CLOB,

  CONSTRAINT pk_testrun PRIMARY KEY (ID),
  CONSTRAINT fk_testrun_testsuite_id FOREIGN KEY (fk_testsuite_id) REFERENCES testsuite (id)
);
