package ru.ckateptb.loreimprove.attribute;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.RandomUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.ckateptb.loreimprove.attribute.implement.AbstractPercentageLoreAttribute;
import ru.ckateptb.loreimprove.attribute.service.LoreAttributeService;
import ru.ckateptb.tablecloth.spring.SpringContext;
import ru.ckateptb.tablecloth.temporary.paralyze.TemporaryParalyze;

@Component
public class ParalyzeLoreAttribute extends AbstractPercentageLoreAttribute implements Listener {
    @Override
    public String getName() {
        return config.getStunName();
    }

    @Override
    public double getDefault() {
        return config.getStunDefault();
    }

    @Override
    public String getDescription() {
        return config.getStunDescription();
    }

    @Override
    public Material getIcon() {
        return Material.ANVIL;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Projectile projectile) {
            damager = (Entity) projectile.getShooter();
        }
        if (damager instanceof Player player) {
            AnnotationConfigApplicationContext context = SpringContext.getInstance();
            LoreAttributeService attributeService = context.getBean(LoreAttributeService.class);
            if (RandomUtils.nextDouble(1, 100) < attributeService.getAttributeAmount(player, this)) {
                if(event.getEntity() instanceof LivingEntity livingEntity) {
                    new TemporaryParalyze(livingEntity, config.getStunDuration());
                }
            }
        }
    }
}
