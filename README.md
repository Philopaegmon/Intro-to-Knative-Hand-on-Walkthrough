# Introduction to Knative Hand-On Walkthrough

## Install needed dependencies
1. Install `docker` as part of Docker Desktop ([Windows](https://docs.docker.com/desktop/install/windows-install/), [macOS](https://docs.docker.com/desktop/install/mac-install/), [Linux](https://docs.docker.com/desktop/install/linux-install/)).
2. Install either [`kind`](https://kind.sigs.k8s.io/docs/user/quick-start#installation) or [`minikube`](https://minikube.sigs.k8s.io/docs/start/) to enable you to run a local Kubernetes cluster with Docker container nodes.
3. Install `kubectl` ([Windows](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/), [macOS](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/), [Linux](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/)), the Kubernetes CLI, to run commands against Kubernetes clusters.
4. Install [`kn`](https://knative.dev/docs/getting-started/quickstart-install/#install-the-knative-cli), the Knative CLI to run commands against the Knative cluster.
5. Install the Knative [quickstart](https://knative.dev/docs/getting-started/quickstart-install/#install-the-knative-quickstart-plugin) plugin.

## Create a Kubernetes Knative cluster using either `kind` or `minikube`
### Using `kind` 
Run sequentially the following commands on the terminal:
 ```
## Install Knative and Kubernetes
kn quickstart kind

## After the plugin is finished, verify you have a cluster called knative
kind get clusters
 ```
### Using `minikube` 
Run sequentially the following commands on the terminal:
```
## Install Knative and Kubernetes
kn quickstart minikube

## Gives you access to the services exposed by the Knative cluster.
## Run on a different terminal window and keep open to allow access.
## For more info visit:
## https://minikube.sigs.k8s.io/docs/handbook/accessing/#using-minikube-tunnel
minikube tunnel --profile knative

## After the plugin is finished, verify you have a cluster called knative
minikube profile list
```

## Build and Push your Docker image to either kind's or minikube's image registry
### Using minikube 
Push your built images directly to the in-cluster image registry.

To point your terminal to use the docker daemon inside minikube run this:

On macOS and Linux:
``` 
eval $(minikube docker-env)
```
On Windows:
```
## Powershell
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
## cmd
@FOR /f "tokens=*" %i IN ('minikube -p minikube docker-env --shell cmd') DO @%i
```
Now any `docker` command you run in this current terminal will run against the docker inside the minikube cluster.

Go to the demo project's root directory and build the docker image.
```
docker build -t dev.local/knative-demo/hello-world -f src/main/docker/Dockerfile.jvm .
```
Check whether the image has been pushed to minikube's image registry.
```
docker image ls -a
```
### Using kind
Go to the demo project's root directory and build the docker image.
```
docker build -t dev.local/knative-demo/hello-world -f src/main/docker/Dockerfile.jvm .
```
Load the docker image onto kind's image registry.
```
kind load docker-image dev.local/knative-demo/hello-world --name knative
```
Check whether the image has been pushed to kind's image registry.
```
docker exec -it knative-control-plane crictl images
```

## Deploying a Knative Service
```
kn service create hello-world \                          
--image dev.local/knative-demo/hello-world:latest \
--pull-policy Never \
--port 8080
```

## Send HTTP request using curl
```
curl --location --request GET 'http://hello-world.default.127.0.0.1.sslip.io/greeting/World' 
```

## Observe autoscaling
```
kubectl get pod -l serving.knative.dev/service=hello-world -w
```