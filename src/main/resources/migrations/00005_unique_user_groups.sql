alter table user_groups ADD CONSTRAINT unique_constraint_on_username_and_groupname UNIQUE(username, groupname);
