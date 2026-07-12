✅ Spring Boot app deployed in Minikube
The application is now running in your local Minikube cluster.

Access URL
Open:

http://192.168.49.2:30080
What was set up
A container image built locally for Minikube
A Kubernetes deployment and NodePort service
Configuration adjusted so the app starts without needing an external Postgres server
Files added/updated
Dockerfile
deployment.yaml
AppConfig.java
application.properties
Useful commands
Check pods:
kubectl get pods,svc
View logs:
kubectl logs deployment/crudsbmsapp
Rebuild and redeploy:
mvn -q -DskipTests package
eval $(minikube docker-env)
docker build -t crudsbmsapp:local .
kubectl apply -f deployment.yaml
If you want, I can next add a PostgreSQL deployment inside Minikube and wire the app to it instead of using the embedded H2 path.

