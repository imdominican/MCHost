package net.hyrical.trevon.Utils;

import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import net.hyrical.trevon.Data.FileManager;
import net.hyrical.trevon.JSON.ServerMeta;
import net.hyrical.trevon.Main;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class DataBase {

    private final Gson gson = new Gson();
    private final ConnectionString connectionString;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    public DataBase(Main main, FileManager fileManager) {
        connectionString = new ConnectionString(fileManager.getConfig().getString("con"));

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        database = mongoClient.getDatabase(fileManager.getConfig().getString("database"));

        collection = database.getCollection("Servers");

    }

    public void insertServerInfo(ServerMeta serverInfo) {

        Document document = Document.parse(gson.toJson(serverInfo));
        collection.insertOne(document);
    }

    public ArrayList<ServerMeta> getServerInfo(String name) { // get by name

        ArrayList<ServerMeta> results = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find(new Document("name", name)).iterator()) {
            while (cursor.hasNext()) {
                results.add(gson.fromJson(cursor.next().toJson(), ServerMeta.class));
            }
        }

        return results;

    }

    public ArrayList<ServerMeta> getServerInfo(UUID uuid) { // get by uuid

        ArrayList<ServerMeta> results = new ArrayList<>();

        try (MongoCursor<Document> cursor = collection.find(new Document("uuid", uuid)).iterator()) {
            while (cursor.hasNext()) {
                results.add(gson.fromJson(cursor.next().toJson(), ServerMeta.class));
            }
        }

        return results;

    }








}
