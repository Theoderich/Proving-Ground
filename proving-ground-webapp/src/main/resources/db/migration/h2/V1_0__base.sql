CREATE SEQUENCE s_id
  START WITH 1;

CREATE TABLE PROJECT (
  id   BIGINT       NOT NULL,
  name VARCHAR(255) NOT NULL,


  CONSTRAINT pk_project PRIMARY KEY (ID),
  CONSTRAINT u_project__name UNIQUE (name)
);

CREATE TABLE BRANCH (
  id            BIGINT       NOT NULL,
  fk_project_id BIGINT       NOT NULL,
  name          VARCHAR(255) NOT NULL,
  status        INT          NOT NULL,

  CONSTRAINT pk_branch PRIMARY KEY (ID),
  CONSTRAINT fk_branch__project__id FOREIGN KEY (fk_project_id) REFERENCES PROJECT (id),
  CONSTRAINT u_branch__name UNIQUE (fk_project_id, name)
);

CREATE TABLE BUILD (
  id           BIGINT       NOT NULL,
  fk_branch_id BIGINT       NOT NULL,
  name         VARCHAR(255) NOT NULL,
  commitId     VARCHAR(250),
  start_time   TIMESTAMP    NOT NULL,
  status       INT          NOT NULL,
  num_total    INT          NOT NULL,
  num_success  INT          NOT NULL,
  num_failed   INT          NOT NULL,
  num_skipped  INT          NOT NULL,
  CONSTRAINT pk_build PRIMARY KEY (ID),
  CONSTRAINT fk_build__branch__id FOREIGN KEY (fk_branch_id) REFERENCES BRANCH (id),
  CONSTRAINT u_build__name UNIQUE (fk_branch_id, name)

);

CREATE TABLE TEST (
  id                    BIGINT       NOT NULL,
  fk_branch_id          BIGINT       NOT NULL,
  fk_build_last_run     BIGINT       NOT NULL,
  fk_build_last_success BIGINT,
  name                  VARCHAR(500) NOT NULL,

  CONSTRAINT pk_test PRIMARY KEY (ID),
  CONSTRAINT fk_test__branch__id FOREIGN KEY (fk_branch_id) REFERENCES BRANCH (id),
  CONSTRAINT fk_test__build__last_run FOREIGN KEY (fk_build_last_run) REFERENCES BUILD (id),
  CONSTRAINT fk_test__build__last_success FOREIGN KEY (fk_build_last_success) REFERENCES BUILD (id),
  CONSTRAINT u_test__name UNIQUE (fk_branch_id, name)
);

CREATE TABLE TEST_RUN (
  id          BIGINT NOT NULL,
  fk_build_id BIGINT NOT NULL,
  fk_test_id  BIGINT NOT NULL,
  result      INT    NOT NULL,
  duration    BIGINT NOT NULL,
  stdout      CLOB,
  stderr      CLOB,

  CONSTRAINT pk_test_run PRIMARY KEY (ID),
  CONSTRAINT fk_test_run__build__id FOREIGN KEY (fk_build_id) REFERENCES BUILD (id),
  CONSTRAINT fk_test_run__test__id FOREIGN KEY (fk_test_id) REFERENCES TEST (id)
);

CREATE TABLE ERROR_INFO (
  id             BIGINT NOT NULL,
  fk_test_run_id BIGINT NOT NULL,
  errorType      CLOB,
  errorMessage   CLOB,
  stacktrace     CLOB,

  CONSTRAINT pk_error_info PRIMARY KEY (ID),
  CONSTRAINT fk_error_info__test_run__id FOREIGN KEY (fk_test_run_id) REFERENCES TEST_RUN (id)
);
