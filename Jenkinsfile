pipeline{
    agent any
    stages{
        stage('Buzz Build') {
            steps {
                 sh './gradlew build'
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