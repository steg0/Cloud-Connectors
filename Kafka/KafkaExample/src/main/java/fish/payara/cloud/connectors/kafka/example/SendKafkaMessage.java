/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package fish.payara.cloud.connectors.kafka.example;

import fish.payara.cloud.connectors.kafka.api.KafkaConnection;
import fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.resource.ConnectionFactoryDefinition;
import javax.resource.spi.TransactionSupport.TransactionSupportLevel;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author Steve Millidge (Payara Foundation)
 */
@ConnectionFactoryDefinition(name = "java:comp/env/KafkaConnectionFactory", 
  description = "Kafka Conn Factory", 
  interfaceName = "fish.payara.cloud.connectors.kafka.api.KafkaConnectionFactory", 
  resourceAdapter = "kafka-rar-0.8.0-SNAPSHOT", 
  minPoolSize = 2, 
  maxPoolSize = 2,
  transactionSupport = TransactionSupportLevel.NoTransaction,
  properties = {"acks=all"
  })
@Stateless
public class SendKafkaMessage {
    
    @Resource(lookup="java:comp/env/KafkaConnectionFactory")
    KafkaConnectionFactory factory;
    
    @Schedule(second = "*/1", hour="*", minute="*")
    public void sendMessage() throws Exception {

        try (KafkaConnection conn = factory.createConnection()) {
            conn.send(new ProducerRecord("test","hello","world"));
        } catch (Exception ex) {
            Logger.getLogger(SendKafkaMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
