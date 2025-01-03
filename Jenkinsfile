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
        writeFile file: 'pipeline.log', text: logString

        def response = httpRequest(
          httpMode: 'POST',
          contentType: 'APPLICATION_FORM_DATA',
          url: "https://7lth4i7d97.execute-api.us-east-1.amazonaws.com/dev/upload-log",
          uploadFile: 'pipeline.log'
        )

        writeFile file: 'response.log', text: response.content
      }
    }
  }
}
