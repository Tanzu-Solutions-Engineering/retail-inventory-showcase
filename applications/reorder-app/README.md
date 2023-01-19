## Docker building image

```shell
mvn install
cd applications/reorder-app
mvn spring-boot:build-image
```

```shell
docker tag reorder-app:0.0.1-SNAPSHOT cloudnativedata/reorder-app:0.0.1-SNAPSHOT
docker push cloudnativedata/reorder-app:0.0.1-SNAPSHOT
```