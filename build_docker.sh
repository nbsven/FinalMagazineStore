sudo rm -rf /home/db/magazinestore
mvn clean -DskipTests=true package
sudo docker build ./ --force-rm --tag=magazine_store
sudo docker rmi $(sudo docker images -aq -f "dangling=true")
sudo docker-compose -f full_docker.yml up
