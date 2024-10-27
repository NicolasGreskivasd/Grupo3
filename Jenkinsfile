pipeline {
    agent any

    stages {
        stage('Test kubectl') {
            steps {
                script {
                    // Usando microk8s kubectl
                    sh 'microk8s kubectl version --client'
                }
            }
        }

        // Outras etapas...
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
    }
}
