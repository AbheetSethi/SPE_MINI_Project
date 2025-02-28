pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'abheet/calculator:latest'
        GITHUB_REPO_URL = 'https://github.com/AbheetSethi/SPE_MINI_Project.git'
        OPTION = 1
        NUMBER = 2
        EXP = 3
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Checkout the code from the GitHub repository
                    git branch: 'master', url: "${GITHUB_REPO_URL}"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh "mvn test"
                }
            }
        }

        stage('Build Docker Image') {
             steps {
                 sh 'docker build -t $DOCKER_IMAGE .'
                 }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        def loginStatus = sh(script: 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin', returnStatus: true)
                        if (loginStatus != 0) {
                            error("❌ Docker login failed. Check credentials and try again.")
                        }

                        def pushStatus = sh(script: 'docker push $DOCKER_IMAGE', returnStatus: true)
                        if (pushStatus != 0) {
                            error("❌ Docker image push failed. Check DockerHub repository permissions.")
                        }
                    }
                }
            }
        }


        stage('Run Ansible Playbook') {
            steps {
                script {
                    ansiblePlaybook(
                        playbook: 'deploy.yml',
                        inventory: 'inventory'
                    )
                }
            }
        }
    }
     post {
            success {
                emailext(
                    to: 'abheeet.sethi@gmail.com',
                    subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """<p>The build and deployment were <b>successful!</b></p>
                             <p>Check the build details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>"""
                )
            }
            failure {
                emailext(
                    to: 'abheeet.sethi@gmail.com',
                    subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """<p>The build or deployment <b>failed!</b></p>
                             <p>Check the build details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>"""
                )
            }
            always {
                cleanWs()
            }
        }
}