
create keyspace if not exists pokerbase with replication = { 'class' : 'SimpleStrategy', 'replication_factor': 1 } ;


CREATE TABLE IF NOT EXISTS pokerbase.handhistory (
      handId TEXT,
      handHistory TEXT,
      PRIMARY KEY (handId)
   );