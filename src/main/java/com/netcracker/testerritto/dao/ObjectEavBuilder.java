package com.netcracker.testerritto.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObjectEavBuilder {

    private BigInteger objectId;
    private BigInteger objectTypeId;
    private BigInteger parentId;
    private String name;
    private String description;
    private List<Attribute> attributes;
    private List<Reference> references;

    @Transactional
    public static class Builder {

        private JdbcTemplate jdbcTemplate;
        private ObjectEavBuilder objectEav;

        private String OBJECTS_INSERT = "insert all\n into objects(object_id, parent_id, object_type_id, name, description)\n" +
                "    values(object_id_PR.nextval, ?, ?, ? || ' ' || object_id_PR.currval, ?)\n";

        private String ATTRIBUTES_INSERT = "into attributes(object_id, attr_id, value, date_value, list_value_id)\n" +
                "    values(object_id_PR.currval, ?, ?, ?, ?)\n";

        private String REFERENCES_INSERT = "into objreference(attr_id, object_id, reference)\n" +
                "    values(?, object_id_PR.currval, ?)\n";

        private String REFERENCES_INSERT_WITH_ID = "into objreference(attr_id, object_id, reference)\n" +
                "    values(?, ?, ?)\n";

        private String MERGE_FIRST_PART_ATTRIBUTE = "merge into attributes att\n" +
                "    using (select object_id from objects where object_id= ? ) obj\n" +
                "        on (att.object_id = obj.object_id and att.attr_id= ? )\n" +
                "when matched then\n";

        private String MERGE_FIRST_PART_REFERENCE = "merge into objreference att\n" +
                "    using (select object_id from objects where object_id= ? ) obj\n" +
                "        on (att.object_id = obj.object_id and att.attr_id= ? )\n" +
                "when matched then\n";

        private String ATTRIBUTES_VALUE_UPDATE = "    update set att.value = ?\n";

        private String ATTRIBUTES_DATE_VALE_UPDATE = "    update set att.date_value = ?\n";

        private String ATTRIBUTES_LIST_VALUE_ID_UPDATE = "    update set att.list_value_id = ?\n";

        private String REFERENCE_UPDATE = "    update set att.reference = ?\n";

        private String MERGE_SECOND_PART_ATTRIBUTE = "when not matched then\n" +
                "    insert (object_id, attr_id, value,date_value,list_value_id)\n" +
                "        values(?, ?, ?, ?, ?);\n";

        private String MERGE_SECOND_PART_REFERENCE = "when not matched then\n" +
                "    insert  (attr_id, object_id, reference)\n" +
                "        values(?, ?, ?);\n";

        private String DELETE_BY_OBJECT_ID = "delete from objects where object_id = ?";

        private String DELETE_BY_PARENT_ID_AND_TYPE = "delete from objects where parent_id = ? and object_type_id = ?";

        private String DELETE_REFERENCE = "delete from objreference where attr_id = ? and object_id = ? and reference = ?";

        private String GET_ID = "select object_id_pr.currval from dual";

        public Builder(JdbcTemplate jdbcTemplate){
            this.objectEav = new ObjectEavBuilder();
            this.objectEav.attributes = new ArrayList<>();
            this.objectEav.references = new ArrayList<>();
            this.jdbcTemplate = jdbcTemplate;
        }

        public Builder setObjectId(BigInteger objectId) {
            objectEav.objectId = objectId;
            return this;
        }

        public Builder setObjectTypeId(BigInteger objectTypeId) {
            objectEav.objectTypeId = objectTypeId;
            return this;
        }

        public Builder setParentId(BigInteger parentId) {
            objectEav.parentId = parentId;
            return this;
        }

        public Builder setName(String name) {
            objectEav.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            objectEav.description = description;
            return this;
        }

        public Builder setReference(BigInteger attributeId, BigInteger referenceId) {
            Reference reference = new Reference(attributeId, referenceId);
            objectEav.references.add(reference);
            return this;
        }

        public Builder setListAttribute(BigInteger attributeId, BigInteger listValueId) {
            Attribute attribute = new Attribute(attributeId, listValueId);
            objectEav.attributes.add(attribute);
            return this;
        }

        public Builder setStringAttribute(BigInteger attributeId, String value) {
            Attribute attribute = new Attribute(attributeId, value);
            objectEav.attributes.add(attribute);
            return this;
        }

        public Builder setDateAttribute(BigInteger attributeId, Date dataValue) {
            Attribute attribute = new Attribute(attributeId, dataValue);
            objectEav.attributes.add(attribute);
            return this;
        }

        @Transactional
        public BigInteger create(){
            String query = this.OBJECTS_INSERT;
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(checkNull(this.objectEav.parentId));
            objects.add(this.objectEav.objectTypeId.toString());
            objects.add(this.objectEav.name);
            objects.add(this.objectEav.description);
            for(Attribute attribute : this.objectEav.attributes){
                query += this.ATTRIBUTES_INSERT;
                objects.add(attribute.attributeId.toString());
                objects.add(attribute.value);
                objects.add(attribute.dateValue);
                objects.add(checkNull(attribute.listValueId));
            }
            for(Reference reference : this.objectEav.references){
                query += this.REFERENCES_INSERT;
                objects.add(reference.attributeId.toString());
                objects.add(reference.referenceId.toString());
            }
            query += "select * from dual";
            this.jdbcTemplate.update(query, objects.toArray());
            return this.jdbcTemplate.queryForObject(GET_ID, BigInteger.class);
        }

        /**
         * CreateReferences
         * @deprecated
         * This method is no longer acceptable to create References.
         * <p> Use {@link Builder#update()} instead.
         */
        @Deprecated
        @Transactional
        public void createReference(){
            String query = "insert all\n";
            ArrayList<Object> objects = new ArrayList<>();
            for(Reference reference : this.objectEav.references){
                query += this.REFERENCES_INSERT_WITH_ID;
                objects.add(reference.attributeId.toString());
                objects.add(this.objectEav.objectId.toString());
                objects.add(reference.referenceId.toString());
            }
            query += "select * from dual";
            this.jdbcTemplate.update(query, objects.toArray());
        }

        @Transactional
        public void update(){
             String query = "begin\n";
             ArrayList<Object> objects = new ArrayList<>();
             for(Attribute attribute : this.objectEav.attributes){
                 query += this.MERGE_FIRST_PART_ATTRIBUTE;
                 objects.add(this.objectEav.objectId.toString());
                 objects.add(attribute.attributeId.toString());
                 if(attribute.value != null){
                     query += this.ATTRIBUTES_VALUE_UPDATE;
                     objects.add(attribute.value);
                     objects.add(this.objectEav.objectId.toString());
                     objects.add(attribute.attributeId.toString());
                     objects.add(attribute.value);
                     objects.add(null);
                     objects.add(null);
                 }
                 else if(attribute.dateValue != null){
                     query += this.ATTRIBUTES_DATE_VALE_UPDATE;
                     objects.add(attribute.dateValue);
                     objects.add(this.objectEav.objectId.toString());
                     objects.add(attribute.attributeId.toString());
                     objects.add(null);
                     objects.add(attribute.dateValue);
                     objects.add(null);
                 }
                 else {
                     query += this.ATTRIBUTES_LIST_VALUE_ID_UPDATE;
                     objects.add(attribute.listValueId.toString());
                     objects.add(this.objectEav.objectId.toString());
                     objects.add(attribute.attributeId.toString());
                     objects.add(null);
                     objects.add(null);
                     objects.add(attribute.listValueId.toString());
                 }
                 query += this.MERGE_SECOND_PART_ATTRIBUTE;
             }
             for(Reference reference : this.objectEav.references){
                 query += this.MERGE_FIRST_PART_REFERENCE;
                 objects.add(this.objectEav.objectId.toString());
                 objects.add(reference.attributeId.toString());
                 query += this.REFERENCE_UPDATE;
                 objects.add(reference.referenceId.toString());
                 query += this.MERGE_SECOND_PART_REFERENCE;
                 objects.add(reference.attributeId.toString());
                 objects.add(this.objectEav.objectId.toString());
                 objects.add(reference.referenceId.toString());
             }
             query += "end;";
             this.jdbcTemplate.update(query, objects.toArray());
        }

        @Transactional
        public void delete(){
            if(objectEav.objectId != null){
                if(this.objectEav.references.size() == 0){
                    String query = this.DELETE_BY_OBJECT_ID;
                    this.jdbcTemplate.update(query, this.objectEav.objectId.toString());
                }
                else{
                    String query = this.DELETE_REFERENCE;
                    this.jdbcTemplate.update(query, this.objectEav.references.get(0).attributeId.toString(),
                                                    this.objectEav.objectId.toString(),
                                                    this.objectEav.references.get(0).referenceId.toString());
                }
            }
            else if(objectEav.parentId != null && objectEav.objectTypeId != null){
                String query = this.DELETE_BY_PARENT_ID_AND_TYPE;
                this.jdbcTemplate.update(query, this.objectEav.parentId.toString(),
                                                this.objectEav.objectTypeId.toString());
            }
        }

        private String checkNull(BigInteger id){
            if(id != null)
                return id.toString();
            else return null;
        }
    }

    private static class Attribute{

        private BigInteger attributeId;
        private String value;
        private Date dateValue;
        private BigInteger listValueId;

        private Attribute(BigInteger attributeId,  BigInteger listValueId){
            this.attributeId = attributeId;
            this.listValueId = listValueId;
        }

        private Attribute(BigInteger attributeId,  Date dateValue){
            this.attributeId = attributeId;
            this.dateValue = dateValue;
        }

        private Attribute(BigInteger attributeId,  String value){
            this.attributeId = attributeId;
            this.value = value;
        }

        private BigInteger getAttributeId() {
            return attributeId;
        }

        public String getValue() {
            return value;
        }

        public Date getDateValue() {
            return dateValue;
        }

        public BigInteger getListValueId() {
            return listValueId;
        }
    }

    private static class Reference{

        private BigInteger attributeId;
        private BigInteger referenceId;

        private Reference(BigInteger attributeId, BigInteger referenceId){
            this.attributeId = attributeId;
            this.referenceId = referenceId;
        }
    }
}
