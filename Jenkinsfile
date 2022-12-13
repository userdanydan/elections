pipeline{
    agent any
    stages{
        stage('Buzz Build') {
            steps {
                 sh  './jenkins/test-all.sh'
                junit '**/surefire-reports/**/*.xml'
            }
        }
        stage('Bees') {
            steps {
                echo 'Bees buzzing'
            }
        }
    }
}