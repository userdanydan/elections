pipeline{
    agent any
    tools {
        gradle '7.6'
    }
    stages{
        stage('Buzz Build') {
            steps {
                sh 'gradle build'
            }
        }
        stage('Bees') {
            steps {
                echo 'Bees buzzing'
            }
        }
    }
}