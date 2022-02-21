CREATE TABLE status_project (
	status_id INT PRIMARY KEY,
	status_name VARCHAR NOT NULL
);

CREATE TABLE projects (
	project_id SERIAL PRIMARY KEY,
	status_project_id INT NOT NULL,
	name VARCHAR NOT NULL,
	description VARCHAR NOT NULL,
	cost DECIMAL,
	start_date DATE NOT NULL,
	duration_time DATE,
	CONSTRAINT fk_status
		FOREIGN KEY(status_project_id)
		REFERENCES status_project(status_id)
		ON DELETE CASCADE
);

CREATE TABLE roles (
	role_id INT PRIMARY KEY,
	role_name VARCHAR NOT NULL
);

CREATE TABLE employees (
	employee_id SERIAL PRIMARY KEY,
	role_id INT NOT NULL,
	CONSTRAINT fk_role
		FOREIGN KEY(role_id)
		REFERENCES roles(role_id)
		ON DELETE CASCADE
);

CREATE TABLE project_employee (
	project_id INT,
	employee_id INT,
	CONSTRAINT fk_project_employees
		FOREIGN KEY(project_id)
		REFERENCES projects(project_id),
		FOREIGN KEY(employee_id)
		REFERENCES employees(employee_id)
		ON DELETE CASCADE
);

INSERT INTO status_project VALUES (1, 'active');
INSERT INTO status_project VALUES (2, 'unactive');
INSERT INTO status_project VALUES (3, 'closed');

INSERT INTO projects(status_project_id, name, description, cost, start_date) VALUES (1, 'Test Project', 'Just an example project', 15000, '2022-01-17');
INSERT INTO projects(status_project_id, name, description, cost, start_date) VALUES (2, 'Closed Project', 'Test of a closed project', 25699.99, '2022-01-26');

INSERT INTO roles VALUES (1, 'PO');
INSERT INTO roles VALUES (2, 'DEV');

INSERT INTO employees (role_id) VALUES (1);
INSERT INTO employees (role_id) VALUES (2);
INSERT INTO employees (role_id) VALUES (1);
INSERT INTO employees (role_id) VALUES (2);
INSERT INTO employees (role_id) VALUES (2);
INSERT INTO employees (role_id) VALUES (2);
INSERT INTO employees (role_id) VALUES (2);
INSERT INTO employees (role_id) VALUES (2);

INSERT INTO project_employee VALUES (1, 1);
INSERT INTO project_employee VALUES (1, 2);
INSERT INTO project_employee VALUES (1, 4);
INSERT INTO project_employee VALUES (2, 3);
INSERT INTO project_employee VALUES (2, 5);
