
podTemplate(label: 'builder',
            containers: [
                    containerTemplate(name: 'jnlp', image: 'larribas/jenkins-jnlp-slave-with-ssh:1.0.0', args: '${computer.jnlpmac} ${computer.name}'),
                    containerTemplate(name: 'docker', image: 'docker', ttyEnabled: true, command: 'cat',
                            envVars: [containerEnvVar(key: 'DOCKER_CONFIG', value: '/tmp/'),]),
                    containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl:v1.8.8', command: 'cat', ttyEnabled: true),
                    containerTemplate(name: 'maven', image: 'maven:alpine', ttyEnabled: true, command: 'cat')
            ],
            volumes: [
                    hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
                    secretVolume(secretName: 'docker-config', mountPath: '/tmp'),
                    secretVolume(secretName: 'kube-config', mountPath: '/root/.kube'),
                    persistentVolumeClaim(claimName: 'mavenrepo-volume-claim', mountPath: '/root/.m2/repository', readOnly: false)
            ]) 
            {

              node('builder') {

                  def DOCKER_HUB_ACCOUNT = 'anilbhagat'
                  def DOCKER_IMAGE_NAME = 'anilbhagat/kite'
                  def K8S_DEPLOYMENT_NAME = 'kite-app'

                  stage('Clone Hugo App Repository') {
                      checkout scm

                      stage('Build Code') {
                        container('maven') {
                          sh 'mvn clean install'
                          }
                        }
              
                      

                      container('docker') {
                          stage('Docker Build & Push Current & Latest Versions') {
                              sh ("docker build -t ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} .")
                              sh ("docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}")
                              sh ("docker tag ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:latest")
                              sh ("docker push ${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:latest")
                          }
                      }

                      container('kubectl') {
                          stage('Deploy New Build To Kubernetes') {
                              sh ("kubectl set image deployment/${K8S_DEPLOYMENT_NAME} ${K8S_DEPLOYMENT_NAME}=${DOCKER_HUB_ACCOUNT}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}")
                          }
                      }

                  }        
              }
            }
