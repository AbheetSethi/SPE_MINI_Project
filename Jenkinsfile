pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'abheetsethi/calculator:latest'
        GITHUB_REPO_URL = 'https://github.com/AbheetSethi/SPE_MINI_Project.git'
        OPTION = 1
        NUMBER = 2
        EXP = 3
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    git branch: 'master', url: "${GITHUB_REPO_URL}"
                }
            }
        }

        stage('Debug Environment') {
            steps {
                sh '''
                    echo "Current user: $(whoami)"
                    echo "Python version: $(python3 --version)"
                    echo "Pip version: $(pip3 --version)"
                    echo "Docker library installed: $(pip3 list | grep docker)"
                    echo "Docker version: $(docker --version)"
                '''
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    try {
                        sh '''
                            # Update package list (with sudo)
                            sudo apt-get update

                            # Install Python 3 and pip (with sudo)
                            sudo apt-get install -y python3 python3-pip

                            # Install Docker Python library
                            pip3 install docker

                            # Verify installation
                            python3 --version
                            pip3 --version
                            pip3 list | grep docker
                        '''
                    } catch (Exception e) {
                        error("❌ Setup Environment failed: ${e.message}")
                    }
                }
            }
        }

        stage('Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Run Tests') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    sh "mvn test"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh "docker build -t ${DOCKER_IMAGE} ."
                    } catch (Exception e) {
                        error("❌ Docker build failed: ${e.message}")
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {
                        def loginStatus = sh(script: "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin", returnStatus: true)
                        if (loginStatus != 0) {
                            error("❌ Docker login failed. Check credentials and try again.")
                        }

                        def pushStatus = sh(script: "docker push ${DOCKER_IMAGE}", returnStatus: true)
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
                    try {
                        ansiblePlaybook(
                            playbook: 'deploy.yml',
                            inventory: 'inventory.ini',
                            extras: '-vvv -e ansible_python_interpreter=/usr/bin/python3'
                        )
                    } catch (Exception e) {
                        error("❌ Ansible playbook execution failed: ${e.message}")
                    }
                }
            }
        }
    }
/*
    post {
    success {
        echo "Sending success email to abheeet.sethi@gmail.com"
        emailext(
            to: 'abheeet.sethi@gmail.com',
            subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """<p>The build and deployment were <b>successful!</b></p>
                     <p>Check the build details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>"""
        )
    }
    failure {
        echo "Sending failure email to abheeet.sethi@gmail.com"
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
*/
}
