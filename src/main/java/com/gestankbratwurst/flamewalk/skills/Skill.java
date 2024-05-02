package com.gestankbratwurst.flamewalk.skills;

public interface Skill<C, T> {

  Class<T> getTriggerType();

  void onAcquire(C caster);

  void onRemove(C caster);

  void onCast(C caster, T trigger);

}
