package org.arquillian.example;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BasketTest {
	
	@Inject
	private Basket basket;
	
	@EJB
	private OrderRepository orderRepo;
	
	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClasses(Basket.class, OrderRepository.class, SingletonOrderRepository.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return jar;
	}

	@Test
    @InSequence(1)
    public void placeOrderShouldAddOrder() {
        basket.addItem("sunglasses");
        basket.addItem("suit");
        basket.placeOrder();
        Assert.assertEquals(1, orderRepo.getOrderCount());
        Assert.assertEquals(0, basket.getItemCount());
        
        basket.addItem("raygun");
        basket.addItem("spaceship");
        basket.placeOrder();
        Assert.assertEquals(2, orderRepo.getOrderCount());
        Assert.assertEquals(0, basket.getItemCount());
    }
    
    @Test
    @InSequence(2)
    public void orderShouldBePersistent() {
        Assert.assertEquals(2, orderRepo.getOrderCount());
    }

}
