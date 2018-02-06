UPDATE testsuite
SET branch = '<NONE>'
WHERE branch IS NULL;

ALTER TABLE testsuite
  ALTER COLUMN branch VARCHAR(250) NOT NULL;