current_branch := latest
build:
	docker build -t rookiefly/mysql:$(current_branch) ./mysql
	docker build -t rookiefly/dubbo-admin:$(current_branch) ./dubbo-admin
	docker build -t rookiefly/sentinel-dashboard:$(current_branch) ./sentinel