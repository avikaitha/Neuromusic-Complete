function result=test(D)

dbname = 'eeg';
username = 'root';
password = '';
driver = 'com.mysql.jdbc.Driver';
dburl = ['jdbc:mysql://localhost:3306/' dbname];


conn = database(dbname, username, password, driver, dburl);
conn.message;
curs = exec(conn,'Select * From Raw_FD');
curs = fetch(curs);
FD = cell2mat(curs.Data(:,2))
label = curs.Data(:,3)

X=hfd(D,256)
Mdl = fitcnb(FD,label);
result = predict(Mdl,X)


