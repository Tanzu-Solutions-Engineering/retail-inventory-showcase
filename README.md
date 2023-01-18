
Starting GemFire

Open Gfsh

```shell
export GEMFIRE_HOME=/Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-9.15.3
cd $GEMFIRE_HOME/bin
./gfsh
```

Start Locator

```shell
start locator --name=locator1 --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1 
```

PDX

```shell
configure pdx --disk-store --read-serialized=true

```

Start Server

```shell
start server --name=server1 --server-port=40404  --locators=127.0.0.1[10334]  --bind-address=127.0.0.1 --hostname-for-clients=127.0.0.1  --jmx-manager-hostname-for-clients=127.0.0.1 --http-service-bind-address=127.0.0.1
```

Create Regions

```shell
run --file=/Users/Projects/VMware/Tanzu/Use-Cases/Vertical-Industries/VMware-Retail/dev/vmware-retail-inventory-showcase/deployments/repositories/gemfire/create_regions.gfsh
```



## User Interface

![ui.png](docs/images/ui.png)

# Submit Transaction Testing



Exchange: retail.transaction

contentType = application/json

```json
{
  "storeId": "001",
  "registerId": "LANE1",
  "total": 3.3,
  "itemId": "SKU-PEANUT-BUTTER"
}
```

With Timestamp

```json
{
  "storeId":"001",
  "registerId":"LANE1",
  "total": 3.33,
  "itemId":"SKU-PEANUT-BUTTER",
  "timestamp":"2023-01-17T10:36:54.82721"
}
```

```json
{
  "storeId":"001",
  "registerId":"LANE1",
  "total": 3.33,
  "itemId":"SKU-PEANUT-BUTTER",
  "timestamp":"2023-01-16T10:36:54.82721"
}
```


# Update StoreProductInventory Testing

Exchange: retail.storeProductInventory

```json
{
  "id": "SKU-PEANUT-BUTTER|001",
  "productId": "SKU-PEANUT-BUTTER",
  "storeId": "001",
  "currentAvailable": 10
}
```


```json
{
  "id": "SKU-PEANUT-BUTTER|001",
  "productId": "SKU-PEANUT-BUTTER",
  "storeId": "001",
  "currentAvailable": 50
}
```