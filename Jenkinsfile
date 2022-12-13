pipeline{
    agent any
    tools {
        gradle '7.6'
    }
    stages{
        stage('Buzz Build') {
            steps {
                sh '''
                    env | grep -e PATH -e JAVA_HOME
                    which java
                    java -version
                '''
            }
        }
        stage('Bees') {
            steps {
                echo 'Bees buzzing'
            }
        }
    }
}