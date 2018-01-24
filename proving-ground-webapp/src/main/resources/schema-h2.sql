DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS testsuite;
DROP TABLE IF EXISTS testrun;

DROP SEQUENCE IF EXISTS s_id;
CREATE SEQUENCE s_id
  START WITH 1;

CREATE TABLE project (
  id     INT                NOT NULL,
  name   VARCHAR(50)        NOT NULL,
  status ENUM ('ok', 'nok') NOT NULL,

  CONSTRAINT pk_project PRIMARY KEY (ID),
  CONSTRAINT u_project_name UNIQUE (name)
);

CREATE TABLE testsuite (
  id            INT                NOT NULL,
  fk_project_id INT                NOT NULL,
  name          VARCHAR(50)        NOT NULL,
  start_time    TIMESTAMP          NOT NULL,
  status        ENUM ('ok', 'nok') NOT NULL,
  num_total     INT                NOT NULL,
  num_success   INT                NOT NULL,
  num_failed    INT                NOT NULL,
  num_skipped   INT                NOT NULL,
  CONSTRAINT pk_testsuite PRIMARY KEY (ID),
  CONSTRAINT fk_testsuite_project_id FOREIGN KEY (fk_project_id) REFERENCES project (id),
  CONSTRAINT u_testsuite_name UNIQUE (fk_project_id, name)
);

CREATE TABLE testrun (
  id              INT                                             NOT NULL,
  fk_testsuite_id INT                                             NOT NULL,
  test_name       VARCHAR(255)                                    NOT NULL,
  result          ENUM ('success', 'failure', 'error', 'skipped') NOT NULL,
  duration        BIGINT                                          NOT NULL,
  output          CLOB,
  errorType       VARCHAR(255),
  errorMessage    VARCHAR(255),
  stacktrace      CLOB,

  CONSTRAINT pk_testrun PRIMARY KEY (ID),
  CONSTRAINT fk_testrun_testsuite_id FOREIGN KEY (fk_testsuite_id) REFERENCES testsuite (id)
);
