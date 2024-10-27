pipeline {
    agent any

    stages {
        stage('Test kubectl') {
            steps {
                script {
                    sh 'kubectl version --client'
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
