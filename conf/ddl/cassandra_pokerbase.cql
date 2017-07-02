
create keyspace if not exists pokerbase with replication = { 'class' : 'SimpleStrategy', 'replication_factor': 1 } ;


CREATE TABLE IF NOT EXISTS pokerbase.hand_history (
      hand_id TEXT,
      hand_history TEXT,
      PRIMARY KEY (hand_id)
   );