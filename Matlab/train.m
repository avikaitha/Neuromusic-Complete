function train(D,mood)
dbname = 'eeg';
username = 'root';
password = '';
driver = 'com.mysql.jdbc.Driver';
dburl = ['jdbc:mysql://localhost:3306/' dbname];

conn = database(dbname, username, password, driver, dburl);
conn.message

 Result = hfd(D,256)
  colnames = {'FD','Mood'};
 data = {Result,mood};
 datainsert(conn,'Raw_FD',colnames,data);
 

