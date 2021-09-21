import com.zugazagoitia.knag.model.User;
import io.quarkus.mongodb.rest.data.panache.PanacheMongoRepositoryResource;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

@Consumes("application/json")
@Produces("application/json")
interface UserResource extends PanacheMongoRepositoryResource<UserRepository,User,ObjectId> {

}
