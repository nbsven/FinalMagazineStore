FROM frolvlad/alpine-oraclejdk8:slim
ADD target/MagazineStore.war MagazineStore.war
ENTRYPOINT ["java","-jar","MagazineStore.war"]