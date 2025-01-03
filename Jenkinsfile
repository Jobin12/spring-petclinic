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

        def response = httpRequest(
          httpMode: 'POST',
          contentType: 'APPLICATION_JSON',
          url: "https://7lth4i7d97.execute-api.us-east-1.amazonaws.com/dev/upload-log",
          requestBody: '{"text": "hello world"}'
        )

        // writeFile file: 'pipeline.log', text: response
        echo "Response: ${response.content}"
      }
    }
  }
}
