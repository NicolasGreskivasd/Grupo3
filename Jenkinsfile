pipeline {
    agent any

    environment {
        DOCKER_REPO = 'NicolasGreskivasd/NicolasGreskivasd-pucpr.github.io' // substitua pelo seu repositório Docker
        KUBECONFIG_CRED = 'kubeconfig' // nome da credencial kubeconfig no Jenkins para o Kubernetes
        DOCKER_CRED = 'docker-hub-credentials' // para o acesso ao Docker Hub
    }

    stages {
        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        sh 'docker build -t $DOCKER_REPO/projeto-web:latest -f Dockerfile.frontend .'
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        sh 'docker build -t $DOCKER_REPO/projeto-spring:latest -f Dockerfile.backend .'
                    }
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CRED) { 
                        sh 'docker push $DOCKER_REPO/projeto-web:latest'
                        sh 'docker push $DOCKER_REPO/projeto-spring:latest'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Usa o kubeconfig armazenado no Jenkins para acessar o cluster Kubernetes
                    withCredentials([file(credentialsId: KUBECONFIG_CRED, variable: 'KUBECONFIG')]) {
                        // Aplica os manifests do Kubernetes para deployment e services
                        sh 'kubectl apply -f k8s/mysql-pv.yaml'
                        sh 'kubectl apply -f k8s/mysql-init-configmap.yaml'
                        sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                        sh 'kubectl apply -f k8s/mysql-service.yaml'
                        sh 'kubectl apply -f k8s/projeto-web.yaml'
                        sh 'kubectl apply -f k8s/projeto-spring.yaml'
                    }
                }
            }
        }
    }
}
