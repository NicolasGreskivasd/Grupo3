pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Tag Existing Images') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        
                        // Tag frontend e backend existentes com a data e hora atual
                        def timestamp = new Date().format("yyyyMMdd-HHmmss")
                        sh "docker pull nicolasgreskiv/pucpr-gh-pages:frontend-latest"
                        sh "docker pull nicolasgreskiv/pucpr-gh-pages:backend-latest"
                        sh "docker tag nicolasgreskiv/pucpr-gh-pages:frontend-latest nicolasgreskiv/pucpr-gh-pages:frontend-${timestamp}"
                        sh "docker tag nicolasgreskiv/pucpr-gh-pages:backend-latest nicolasgreskiv/pucpr-gh-pages:backend-${timestamp}"
                        
                        // Push das imagens com a tag de backup
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:frontend-${timestamp}"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:backend-${timestamp}"
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        docker.build("nicolasgreskiv/pucpr-gh-pages:frontend-latest", "--no-cache -f Dockerfile .")
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        docker.build("nicolasgreskiv/pucpr-gh-pages:backend-latest", "--no-cache -f Dockerfile .")
                    }
                }
            }
        }

        stage('Push New Images to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        // Push das novas imagens "latest"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:frontend-latest"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:backend-latest"
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        // Aplicar configurações do Kubernetes
                        sh "microk8s kubectl apply -f k8s/create-base-configmap.yaml"
                        sh "microk8s kubectl apply -f k8s/database-pv.yaml"
                        sh "microk8s kubectl apply -f k8s/database-service.yaml"
                        sh "microk8s kubectl apply -f k8s/database-statefulset.yaml"
                        sh "microk8s kubectl apply -f k8s/backend-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/backend-service.yaml"
                        sh "microk8s kubectl apply -f k8s/frontend-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/frontend-service.yaml"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
    }
}
