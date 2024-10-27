pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        
                        // Renomear as imagens frontend e backend como backup
                        def repo = "nicolasgreskiv/pucpr-gh-pages"
                        sh """
                            # Renomear frontend-latest para frontend-backup, se existir
                            docker pull ${repo}:frontend-latest || true
                            docker tag ${repo}:frontend-latest ${repo}:frontend-backup || true
                            docker rmi ${repo}:frontend-latest || true
                            
                            # Renomear backend-latest para backend-backup, se existir
                            docker pull ${repo}:backend-latest || true
                            docker tag ${repo}:backend-latest ${repo}:backend-backup || true
                            docker rmi ${repo}:backend-latest || true

                            # Deletar imagens antigas de backup, mantendo apenas a mais recente
                            docker images ${repo} --format '{{.Repository}}:{{.Tag}}' | grep '-backup' | sort | head -n -1 | xargs -r docker rmi || true
                        """
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        def newTag = "frontend-${new Date().format("yyyyMMdd-HHmmss")}"
                        docker.build("nicolasgreskiv/pucpr-gh-pages:${newTag}", "-f Dockerfile .")
                        docker.tag("nicolasgreskiv/pucpr-gh-pages:${newTag}", "nicolasgreskiv/pucpr-gh-pages:frontend-latest")
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        def newTag = "backend-${new Date().format("yyyyMMdd-HHmmss")}"
                        docker.build("nicolasgreskiv/pucpr-gh-pages:${newTag}", "-f Dockerfile .")
                        docker.tag("nicolasgreskiv/pucpr-gh-pages:${newTag}", "nicolasgreskiv/pucpr-gh-pages:backend-latest")
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        // Push das novas imagens com tags de build e latest
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:frontend-latest"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:backend-latest"
                    }
                }
            }
        }

        stage('Test kubectl') {
            steps {
                script {
                    sh 'microk8s kubectl version --client'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
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
