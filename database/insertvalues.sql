-- Clear previous values
TRUNCATE acl;
TRUNCATE departments;
TRUNCATE deptresponsibilities;
TRUNCATE directories;
TRUNCATE documents;
TRUNCATE pagepermissions;
TRUNCATE pages;
TRUNCATE rolerequests;
TRUNCATE systemlog;
TRUNCATE userrole;
TRUNCATE users;
TRUNCATE usertype;

-- Create some user accounts with default passwords set as password.  NOTE: These passwords should be salted and hashed (but that's not ready yet)
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('guest@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('sysadmin@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('regular@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('dept@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('corp@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('regular2@email.com', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '1');
INSERT INTO `mydb`.`users` (`email`, `password`, `active`) VALUES ('newuser@asu.edu', 'do/dBvtrhtdf3/3A57n8ZNzst7y5g2waoXLJb69y1I8=$QOqMQVhga9LC29Swb95qgFtqUu4BDe/9+J7bhmg/zXs=', '0');

-- Departments (static info mostly, usef for joins and output)
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Human Resources");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Logistic and supply");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("IT support");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Sales and promotion");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Research & Development");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Finance");
INSERT INTO `mydb`.`departments` (`deptname`) VALUES ("Company Management");

-- Assign roles for each of these users
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='guest@email.com'), 2, null);
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='sysadmin@email.com'), 3, null);
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='regular@email.com'), 4, null);
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='dept@email.com'), 5, null);
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='corp@email.com'), 6, null);
INSERT INTO `mydb`.`userrole` (`userid`, `usertype`, `department`) VALUES ((SELECT userid FROM users WHERE email='regular2@email.com'), 4, null);

-- Insert static values for each of the usertypes (usefull when we want a text output on a join)
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('temp');
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('guest');
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('systemAdmin');
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('regularEmployee');
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('DeptManager');
INSERT INTO `mydb`.`usertype` (`rolename`) VALUES ('CorpManager');

-- New user is requesting a role for regular employee
INSERT INTO `mydb`.`rolerequests` (`requestinguser`, `requestedrole`) VALUES ((SELECT userid FROM users WHERE email='newuser@asu.edu'), 4);

-- Pages (these should be the same names as the ModelAndView string you return).
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("index");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("login");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("login/error");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("login/signout");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("unauthorized");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("share");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/upload");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/get");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/delete");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/checkout");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/checkin");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("document/share");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("revisions");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("directory");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("directory/create");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("directory/delete");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/activate");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/deactivate");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/createuser");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/deleteuser");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/updateuser");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("sysadmin/viewlog");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("upload");

INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("profile");
INSERT INTO `mydb`.`pages` (`modelandviewname`) VALUES ("profile/modifypswd");



-- Only guests, regular employees, dept and corp managers can upload/update
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='upload'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='upload'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='upload'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='upload'), 6);


-- Only SysAdmin can access the admin panel
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/activate'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/deactivate'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/createuser'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/deleteuser'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/updateuser'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='sysadmin/viewlog'), 3);

-- All usertypes have access to the index (first) page.
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='index'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='index'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='index'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='index'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='index'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/upload'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/upload'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/upload'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/upload'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/get'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/get'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/get'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/get'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/delete'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/delete'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/delete'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/delete'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkout'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkout'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkout'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkout'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkin'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkin'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkin'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/checkin'), 6);

-- Only guests, regular employees, dept and corp managers can ever list documents
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/share'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/share'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/share'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='document/share'), 6);

-- Only guests, regular employees, dept and corp managers can view revisions
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='revisions'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='revisions'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='revisions'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='revisions'), 6);

-- Only guests, regular employees, dept and corp managers can ever manipulate directories
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory'), 6);

INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/create'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/create'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/create'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/create'), 6);

INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/delete'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/delete'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/delete'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='directory/delete'), 6);

-- Everyone can be unauthorized to access some resource, all users get to access that page :P
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 1);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='unauthorized'), 6);

-- Everyone can access the login page (now only displays errors on login, and probaly will do logout success message.
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 1);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='login'), 6);

-- Only guests, regular employees, dept and corp managers can share documents (actually, can guests share their shared docs??)
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='share'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='share'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='share'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='share'), 6);

-- Everyone can change their passwords
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 1);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile/modifypswd'), 6);

-- Everyone can view their profile information
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 1);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 2);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 3);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 4);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 5);
INSERT INTO `mydb`.`pagepermissions` (`pageid`, `userrole`) VALUES ((SELECT pageid FROM pages WHERE modelandviewname='profile'), 6);

-- Assign departments to users who manage them
INSERT INTO `mydb`.`deptresponsibilities` (`userid`, `responsabledept`) VALUES ((SELECT userid FROM users WHERE email='dept@email.com'), (SELECT deptid FROM departments WHERE deptname='Human Resources'));
INSERT INTO `mydb`.`deptresponsibilities` (`userid`, `responsabledept`) VALUES ((SELECT userid FROM users WHERE email='corp@email.com'), (SELECT deptid FROM departments WHERE deptname='IT support'));

-- Add a directory
INSERT INTO `mydb`.`directories` (`dirname`, `parentfolder`, `owner`) VALUES ('myfolder', null, 1);

-- Allowed filetypes
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('image/gif');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('image/jpeg');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('image/pjpeg');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('image/png');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('image/tiff');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/zip');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/pdf');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('text/plain');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/x-tika-msoffice');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/msword');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/octet-stream');
INSERT INTO `mydb`.`whitelist` (filetype) VALUES ('application/x-tika-ooxml');