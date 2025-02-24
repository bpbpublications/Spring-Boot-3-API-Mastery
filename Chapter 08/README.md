# Chapter 08

In this chapter, we will integrate observability and monitoring into edge service and catalog service microservices. \
We will use Spring Boot Actuator with the following open-source tools:
- Loki - for log storing.
- Prometheus - for storing metrics
- Tempo - for storing traces
- Grafana - for visualization of metrics and traces


## Run the projects

From the infra/docker path, run:
```bash
docker compose up -d
```

Then, run the follow microservices:
- catalog service
- edge service

From your browser, open an incognito tab and type in the following URL: http://localhost:8090/products/LAP-ABCD. \
You will be redirected to the Keycloak login. After authenticating, you will see the API response correctly.

## See Grafana dashboards
Open Grafana's home page at http://localhost:3000