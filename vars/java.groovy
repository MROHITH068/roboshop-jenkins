pipeline
        {
            agent{
                node {label 'workstation'}
            }

            stages{
                stage('Build'){
                    steps{
                        sh 'mvn package'
                    }
                }

                stage('Unit tests'){
                    steps{
                        echo 'Unit tests'
                        //sh 'mvn test'
                    }
                }

                stage('Code Analysis'){
                    steps{
                        sh 'sonar-scanner -Dsonar.host.url=http://172.31.20.113:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=shipping -Dsonar.java.binaries=target -Dsonar.qualitygate.wait=true'
                    }
                }

                stage('Security Scans'){
                    steps{
                        echo 'Security Scans'
                    }
                }

                stage('Publish a Artifact'){
                    steps{
                        echo 'Publish a Artifact'
                    }
                }
            }
        }