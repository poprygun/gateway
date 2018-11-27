# Spring Cloud Gateway example

## Prerequisits

```bash
brew install httpie
brew install watch
```

## Start API

```bash
cd tracks-api
mvn spring-boot:run
```

## Start Gateway Server

```bash
cd tracks-gateway
mvn spring-boot:run
```

## This call should access unlimited tracks endpoint

```bash
http :8080 Host:api.pathfinder.io
```

## This call should access limited tracks endpoint
```bash
http :8080 Host:limited-api.pathfinder.io
```

## Demo rate limiter

```bash
watch -n 0.1 http -h :8080 Host:busy-api.pathfinder.io X-API-KEY:zztop1 
```