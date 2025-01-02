pipeline {
  agent any

  stages {
    stage('Unit Test') {
      steps {
        script {
          sh 'fail here'
          sh './mvnw test'
        }
      }
    }

    stage('Build') {
      steps {
        script {
          sh './mvnw clean package'
        }
      }
    }

    stage('Containerization') {
      steps {
        script {
          sh """
            docker build -t jobin589/spring-petclinic:${env.BUILD_NUMBER} .
            docker push myimage
          """
        }
      }
    }
  }

  post {
    failure {
      script {
        def log = currentBuild.rawBuild.getLog(Integer.MAX_VALUE)
        def logString = log.join("\n")
        writeFile file: "Build-${env.BUILD_NUMBER}.log", text: logString

        def logFile = new File("${env.WORKSPACE}/Build-${env.BUILD_NUMBER}.log").text

        def response = httpRequest(
          httpMode: 'PUT',
          contentType: 'TEXT_PLAIN',
          url: "https://o7jlq66p7i.execute-api.us-east-1.amazonaws.com/dev/jenkins-logs-test-bucket/Build-${env.BUILD_NUMBER}.log",
          requestBody: logFile
        )
      }
    }
  }
}
