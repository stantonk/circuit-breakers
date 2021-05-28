build:
	mvn clean package
	docker build . -t testservice