apiVersion: apps/v1
kind: Deployment
metadata:
  name: proving-ground-webapp
  labels:
    app: proving-ground
spec:
  replicas: 1
  selector:
    matchLabels:
      app: proving-ground
  template:
    metadata:
      labels:
        app: proving-ground
    spec:
      containers:
      - name: proving-ground-webapp
        image: ${DOCKER_REGISTRY_URL}/${DOCKER_REGISTRY_REPO}/proving-ground-webapp:latest
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          protocol: TCP
        resources:
          requests:
            cpu: 1000m
            memory: 1Gi
          limits:
            memory: 1Gi
