# save the version in src/version.properties
git rev-parse --short=6  HEAD |sed 's/^/Revision=/' > src/ver.properties
# svn info |grep 'Last Changed Rev:'|sed  's/Last Changed Rev:/Revision=/' > src/ver.properties
mvn clean package

