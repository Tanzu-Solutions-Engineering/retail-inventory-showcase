



Generate password
```shell
java -DCRYPTION_KEY=PIVOTAL -classpath /Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/deployments/gemfire-server/lib/nyla.solutions.core-1.5.1.jar nyla.solutions.core.util.Cryption $1
```


```shell
export CLASSPATH="/Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/components/gemfire-extensions-core/build/libs/gemfire-extensions-core-1.2.1-SNAPSHOT.jar:/Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/components/gemfire-rabbitmq/build/libs/gemfire-rabbitmq-1.0.0.jar:/Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/gemfire-extensions/deployments/gemfire-server/lib/*"

export JDBC_URL=jdbc:postgresql://localhost:5432/postgres
export JDBC_DRIVER_CLASS=org.postgresql.Driver
export JDBC_USERNAME=postgres
export JDBC_PASSWORD=CRYPTED_PASSWORD_HERE
```

```shell
cd /Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-9.15.3/bin
./gfsh
```
```shell
start locator --name=locator1 --port=10334 --locators="127.0.0.1[10334],127.0.0.1[10434]" --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1
```

```shell
configure pdx --disk-store --read-serialized=true
```

```shell
start server --name=server1 --initial-heap=500m --max-heap=500m  --locators="127.0.0.1[10334]"  --server-port=40401 --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1 --start-rest-api=true --http-service-bind-address=127.0.0.1 --http-service-port=9090  --J=-Dgemfire-for-redis-port=6379 --J=-Dgemfire-for-redis-enabled=true --J=-DCRYPTION_KEY=PIVOTAL  --include-system-classpath=true --J=-Dconfig.properties=/Users/Projects/VMware/Tanzu/Use-Cases/Vertical-Industries/VMware-Retail/dev/vmware-retail-inventory-showcase/docs/demo/local/gf-jdbc-rabbit.properties
```


```shell
create async-event-queue --id=rabbit --parallel=true --enable-batch-conflation=true --batch-size=10 --batch-time-interval=10 --persistent=true --disk-synchronous=true --forward-expiration-destroy=true --max-queue-memory=10 --dispatcher-threads=5  --listener=com.vmware.data.solutions.rabbitmq.gemfire.RabbitAsyncEventListener

```

```shell
create region --name=Product  --type=PARTITION --cache-loader=com.vmware.data.services.gemfire.integration.jdbc.JdbcJsonPdxLoader --cache-writer=com.vmware.data.services.gemfire.integration.jdbc.JdbcJsonCacheWriter --async-event-queue-id=rabbit
```


# GemFire Rest Apps


```shell
java -jar applications/product-service-app/target/product-service-app-0.0.1-SNAPSHOT.jar --server.port=9090
```


```shell
open http://localhost:9090
```


In Postgres

```shell
psql -d postgres -U postgres
```


```shell
alter async-event-queue --id=rabbit --pause-event-processing=false --batch-size=100
```


In gfsh

http://localhost:9090/geode/swagger-ui/index.html


```shell
curl -X 'GET' \
  'http://localhost:9090/geode/v1/customers?limit=50&keys=jdoe@vmware.com' \
  -H 'accept: application/json;charset=UTF-8'
```



In GemFire

```shell
query --query="select * from /Product where id = 'sku-1'"
```

```shell
query --query="select * from /Product where id = 'sku-peanut-butter'"
```

In Postgres

```sql
select id, 
    data->'name' name, 
    data->'price' price,
    data->'details' details,
    data->'warnings' warnings,
    data->'nutrition'->'sodium' nutri_sodium,
    data->'nutrition'->'sugars' nutri_sugars,
    data->'nutrition'->'protein' nutri_protein,
    data->'nutrition'->'calories' nutri_nutrition,
    data->'nutrition'->'cholesterol' nutri_cholesterol,
    data->'nutrition'->'totalFatAmount' nutri_totalFatAmount,
    data->'nutrition'->'totalCarbohydrate' nutri_totalCarbohydrate
from products;
```

```roomsql
select * from products where id = 'sku-1';

select * from products where id = '"sku-1"';
```


------------------------





```graphql
mutation SaveProduct ($id: String!, $name: String!, $price: Float, $details: String, $ingredients: String, $directions: String, $warnings: String,
    $quantityAmount: String, $totalFatAmount: Int,
    $cholesterol: Int, $sodium: Int,
    $totalCarbohydrate: Int, $sugars: Int, $protein: Int,
    $calories: Int) {
    saveProduct(id: $id,
        name: $name,
        price: $price,
        details : $details,
        ingredients : $ingredients,
        directions : $directions,
        warnings : $warnings,
        quantityAmount : $quantityAmount,
        totalFatAmount: $totalFatAmount,
        cholesterol: $cholesterol,
        sodium: $sodium,
        totalCarbohydrate: $totalCarbohydrate,
        sugars: $sugars,
        protein: $protein,
        calories: $calories) {
        id : id,
        name : name,
        price : price,
        details : details,
        ingredients : ingredients,
        directions : directions,
        warnings : warnings,
        quantityAmount : quantityAmount
    }
}

```

Variables

```graphql
{
    "id" : "sku-peanut-butter",
    "name" : "Peanut Butter",
    "price" : 3.3,
    "details" : "details",
    "ingredients" : "ingredients",
    "directions" : "directions",
    "warnings" : "warnings",
    "quantityAmount" : "quantityAmount",
    "totalFatAmount": 3,
    "cholesterol": 3,
    "sodium": 3,
    "totalCarbohydrate": 3,
    "sugars": 3,
    "protein": 3,
    "calories": 3
}
```



```shell
query {
    findProductById(id: "sku-peanut-butter") {
        name,
        id
    }
}
```
